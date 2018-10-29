import java.util.ArrayList;
import java.io.*;
import java.net.URL;
import java.util.TreeMap;
import java.net.HttpURLConnection;

class OnlineThesaurus extends MemoryThesaurus
{
    protected int timeout;

    public OnlineThesaurus(int t)
    {
        timeout = t;
        dictionary = new TreeMap<String, ArrayList<String>>();
    }

    protected int getTimeout() { return timeout; }
    protected void setTimeout(int t) { timeout = t; }

    public ArrayList<String> getSynonyms(String word, int num)
    {
        for(int i = 0; i < word.length(); i++)
            if(!Character.isLetter(word.charAt(i)))
                return new ArrayList<String>();

        ArrayList<String> synonyms;

        if((synonyms = dictionary.get(word)) != null) {
            if(synonyms.size() >= num)
                return new ArrayList<String>(synonyms.subList(0, num));
        }
        else {
            synonyms = new ArrayList<String>();
            dictionary.put(word, synonyms);
        }
        
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL("http://www.thesaurus.com/browse/" + word.toLowerCase());
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(timeout);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while((line = in.readLine()) != null)
                content.append(line);

            in.close();
            con.disconnect();
        }
        catch(Exception e) {
              System.out.println("Could not get webpage");
        //    e.printStackTrace();
        }

        String contentString = content.toString();
        System.out.println(contentString);


        int start = -1;
        while(synonyms.size() < num && (start = contentString.indexOf("etbu2a31", start + 1)) != -1) {
            int word_start = contentString.indexOf(">", start);
            if(word_start != -1) {
                int word_end = contentString.indexOf("<", word_start + 1);
                if(word_end != -1) {
                    String synonym = contentString.substring(word_start + 1, word_end);
                    System.out.println(synonym);
                    if(synonyms.indexOf(synonym) == -1)
                        synonyms.add(synonym);
                }
            }
        }

        return synonyms;
    }

    public static void main(String[] args)
    {
        if(args.length < 1)
            return;

        Thesaurus t = new OnlineThesaurus(1000);
        
        String fileText = FileReadPlz.read(args[0]);
        StringBuilder outputText = new StringBuilder();

        System.out.println("\n\n\n");

        int start = 0;
        int end;
        while((end = fileText.indexOf(" ", start)) != -1) {
            String substr = fileText.substring(start, end);
            ArrayList<String> words = t.getSynonyms(substr, 3);
           
            String str; 
            if(words.size() > 0)
                str = words.get((int)(Math.random() * words.size())) + " ";
            else
                str = substr + " ";

            outputText.append(str);
            System.out.print(str);

            start = end + 1;
        }
    }
}
