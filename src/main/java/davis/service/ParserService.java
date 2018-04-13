package davis.service;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

@Service
public class ParserService {

    private LexicalizedParser lp = null;

    private CRFClassifier segmenter;

    @PostConstruct
    public void init() {
        Properties props = new Properties();

        props.setProperty("sighanCorporaDict", "data");

        Resource distChris6 = new ClassPathResource("data/dict-chris6.ser.gz");
        try {
            props.setProperty("serDictionary", distChris6.getURL().toString());
            props.setProperty("inputEncoding", "UTF-8");
            props.setProperty("sighanPostProcessing", "true");

            this.segmenter = new CRFClassifier<>(props);
            Resource ctbResource = new ClassPathResource("data/ctb.gz");
            segmenter.loadClassifierNoExceptions(ctbResource.getURL().toString(), props);

            Resource resource = new ClassPathResource("chinesePCFG.ser.gz");


            this.lp = LexicalizedParser.loadModel(resource.getURL().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<String> segment(String str){
        return this.segmenter.segmentString(str);
    }

    public Tree parse(String str){
        return this.lp.parse(str);
    }
}
