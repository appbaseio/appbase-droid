package elasticsearchlibrary;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tirth Shah on 10-05-2016.
 */


public class Trial {
    AppbaseClient elastic;

    public Trial(AppbaseClient elastic) {
        this.elastic = elastic;
    }

    String json = "{" +
            "\"user\":\"kimchy\"," +
            "\"postDate\":\"2013-01-30\"," +
            "\"message\":\"trying out Elasticsearch\"" +
            "}";
    String[] types={"books","departments"};
    static ArrayList<String> createdIds= new ArrayList<String>();
    
    static Random r=null;
    public static String generateId(){
        if(r==null){
            r=new Random();
        }
        int n=r.nextInt(5)+5;
        String id="";
        for (int i = 0; i < n + 1; i++) {
            id+=(char)(r.nextInt(25)+97)+"";
        }
        createdIds.add(id);
        return id;
    }
    public String generateJsonDoc(){
        return "{" +
                "\"user\":\""+generateId()+"\"," +
                "\"postDate\":\""+generateId()+"\"," +
                "\"message\":\""+generateId()+"\"" +
                "}";
    }

            /*
    TransportClient client;
	IndexResponse response = client.prepareIndex("twitter", "tweet")
	        .setSource(json)
	        .get();*/

    
    public void trial(){
    	for (int i = 0; i < 3; i++) {
        	tryIndex();	
		}
    	tryDelete();
    	
    	
    }
    public void tryIndex(){
        if(r==null){
            r=new Random();
        }
        System.out.println(elastic.index(types[r.nextInt(1)],generateId(),generateJsonDoc()));
    }
    public void tryDelete(){
        if(createdIds.size()>=1){
        	
        	String value=createdIds.remove(r.nextInt(createdIds.size()));
            elastic.delete(types[r.nextInt(1)],value);

        }

    }
    public void tryBulk(){

    }
    public void tryUpdate(){

    }
    public void trySearch(){

    }
    public void tryGetStream(){

    }
    
    public String tryGetTypes(){
    	return elastic.getTypes();
    	
    }
    
    public void trySearchStream(){

    }
    public void trySearchStreamToUrl(){

    }
}
