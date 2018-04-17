package davis.service;

import davis.bo.Match;
import davis.dao.GrammarRepository;
import davis.entity.Grammar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RuleService {

    @Autowired
    private GrammarRepository grammarRepository;

    private List<Grammar> grammarList=new ArrayList<Grammar>();

    @PostConstruct
    public void init() {
        grammarList=this.grammarRepository.findAll();
    }

    public List<Grammar> matchCheck(String result){
        List<Grammar> matchList = new ArrayList<>();

        grammarList.forEach(g->{
            // 比對每一條規則，先用比對中一條就跳出

            // 先找出 anchors
            String []anchors = g.getAnchors().split("/");
            boolean anchorMatch = false;

            // 有幾種不一樣的 anchor words
            for(String anchor:anchors){
                // 要檢查多個字段
                if(anchor.contains("...")){
                    String regularRule =anchor.replace("...",".+");
                    Pattern r = Pattern.compile(regularRule);

                    // Now create matcher object.
                    Matcher m = r.matcher(result);

                    System.out.println("matchCheck input : "+result);
                    System.out.println("matchCheck regularRule : "+regularRule);

                    if(m.find()){
                        anchorMatch=true;
                        break;
                    }
                }
                // 直接包含
                else if(result.contains(anchor)){
                    anchorMatch=true;
                    break;
                }
            }
            // 如果有比對到，再去確認 structure

            boolean structureMatch = false;

            if(StringUtils.isEmpty(g.getStructure())){
                structureMatch = true;
            }else{
                if(anchorMatch){
                    // TODO 要實作結構判斷
                    structureMatch = true;
                }
            }

            // 是否要加入 match object
            if(anchorMatch && structureMatch){
                matchList.add(g);
            }
        });

        return matchList;
    }

}
