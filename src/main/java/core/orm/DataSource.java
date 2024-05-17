package core.orm;




import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataSource {
    private String url;
    private String username;
    private String password;
    private Map<String, String> properties = new HashMap<String, String>();

    public DataSource() {
        Scanner scanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream("proprieties.txt"));
        String line;
        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            String[] parts = line.split("=", 2);
            if (parts.length == 2){
                String key = line.substring(0, line.lastIndexOf('=')).strip();
                String value = line.substring(line.lastIndexOf('=')+1).strip();
                properties.put(key, value);
            }else
                throw new RuntimeException("incorrect properties file");


        }
        setProperties();
        scanner.close();
    }

    private void setProperties(){

        url = properties.get ("myorm.datasource.url");
        username = properties.get ("myorm.datasource.username");
        password = properties.get ("myorm.datasource.password");
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
