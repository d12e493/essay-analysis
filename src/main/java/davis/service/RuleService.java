package davis.service;

import davis.bo.Match;
import davis.dao.GrammarRepository;
import davis.entity.Grammar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {

    @Autowired
    private GrammarRepository grammarRepository;

    private List<Grammar> grammarList=new ArrayList<Grammar>();

    @PostConstruct
    public void init() {
       this.grammarRepository.findAll();
    }

    public List<Match> matchCheck(String result){
        List<Match> matchList = new ArrayList<Match>();

        grammarList.forEach(g->{
            // 比對每一條規則，先用比對中一條就跳出

            // 先找出 anchors
            String []anchors = g.getAnchors().split("//");
            System.out.println("anchors : "+anchors);

            boolean anchorMatch = false;

            for(String anchor:anchors){
                if(result.contains(anchor)){
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
                Match match = new Match();
                match.setGid(g.getGid());
                matchList.add(match);
            }
        });

        return matchList;
    }

}
