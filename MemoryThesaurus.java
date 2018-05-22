import java.util.Map;
import java.util.ArrayList;

abstract public class MemoryThesaurus implements Thesaurus
{
    protected Map<String, ArrayList<String>> dictionary;
}
