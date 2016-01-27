package top.devgo.coupon;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import top.devgo.coupon.utils.JsonUtil;
import top.devgo.coupon.utils.TextUtil;

public class SMZDM2 {
	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
//		HttpGet httpget = new HttpGet("http://www.smzdm.com/json_more?timesort=255070648939");
//		httpget.setHeader("Host", "www.smzdm.com");
//		httpget.setHeader("Referer", "http://www.smzdm.com/");
//		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");
//		httpget.setHeader("X-Requested-With", "XMLHttpRequest");
		
		
		HttpUriRequest request = RequestBuilder
				.get()
				.setUri("http://www.smzdm.com/json_more")
				.addParameter("timesort", "955070648939")
				.setHeader("Host", "www.smzdm.com")
				.setHeader("Referer", "http://www.smzdm.com/")
				.setHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36")
				.setHeader("X-Requested-With", "XMLHttpRequest").build();
		
		CloseableHttpResponse response = null;
		try {
//			response = httpclient.execute(httpget);
			response = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			HttpEntity entity = response.getEntity();
			
			
			if (entity != null) {
				Header contentType = entity.getContentType();
				String encoding = "utf-8";
				if(contentType != null){
					String type = contentType.getValue();
					if(type != null){//Content-Type:text/html;charset=utf-8;
						encoding = type.substring(type.lastIndexOf("charset=") + "charset=".length());
						int pos = encoding.lastIndexOf(";");
						if(pos > -1){
							encoding = encoding.substring(0, pos);
						}
					}
				}
//				System.out.println(encoding);
				
				String htmlStr = EntityUtils.toString(entity, encoding);
				htmlStr = TextUtil.decodeUnicode(htmlStr);
//				System.out.println(htmlStr);
				
				htmlStr = JsonUtil.formateDoubleQuotationMarks(htmlStr);
				
				System.out.println(htmlStr);

				//GSON从json转java不好使
//				Gson gson = new Gson();
//				List<ZDMItem> lists = gson.fromJson("[{\"article_id\":\"379261\",\"article_title\":\"VERA WANG 王薇薇\"}]", new TypeToken<List<ZDMItem>>(){}.getType());
				
				ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
				
				mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;  
				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
				
//				JavaType javaType = mapper.getTypeFactory().constructParametrizedType(List.class, List.class, ZDMItem.class);
//				List<ZDMItem> lists = mapper.readValue(htmlStr, javaType);
//				System.out.println(lists.size());
				
				JsonNode root = mapper.readTree(htmlStr);
				System.out.println(root.size());
				
				for (int i = 0; i < root.size(); i++) {
					JsonNode item = root.get(i);
//					Iterator<Entry<String, JsonNode>> it = item.fields();
//					while (it.hasNext()) {
//						Entry<String, JsonNode> entry = (Entry<String, JsonNode>) it.next();
//						
//						System.out.print(entry.getKey()+" : "+entry.getValue());
//					}
//					System.out.print("\n");
					System.out.println(item.get("article_channel").asText());
				}
				
			}	
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}
	
	
}