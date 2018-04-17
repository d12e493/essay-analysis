package davis.controller;

import davis.bo.Match;
import davis.bo.MatchResponse;
import davis.entity.Grammar;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import davis.service.ParserService;
import davis.service.RuleService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/analysis")
public class EssayController {

    @Autowired
    private ParserService parseService;

    @Autowired
    private RuleService ruleService;

    @RequestMapping(value={"", "/"})
    public String index() {
        return "analysis";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String analysis(){

        return "ok";
    }

    @RequestMapping(value="/input",method = RequestMethod.POST)
    @ResponseBody
    public List<MatchResponse> analysisInput(@RequestBody String input){
        List<MatchResponse> response = new ArrayList<>();

        System.out.println("input------");
        System.out.println(input);

        String[] essays = input.split("，|、|；|。");

        System.out.println("after split");

        for(String e:essays){
            System.out.println(e);
            MatchResponse rep = new MatchResponse();
            rep.setSentence(e);

            // 進行斷詞
            List list = parseService.segment(e);

            String afterE = StringUtils.join(list," ");

            Tree parse = parseService.parse(afterE);

            List<Grammar> matchList = ruleService.matchCheck(parse.toString());
            if(matchList.size()>0){
                rep.setMatchList(matchList);
            }
            response.add(rep);
        }

        return response;
    }

    private List<Tree> getNodeList(Tree parse){
        List<Tree> list = recursive(parse);
        return list;
    }

    private List<Tree> recursive(Tree tree){
        List<Tree> list = new ArrayList<>();

        LabeledScoredTreeNode treeNode = (LabeledScoredTreeNode)tree;

        LabeledScoredTreeNode node = new LabeledScoredTreeNode();
        node.setLabel(treeNode.label());
        node.setValue(treeNode.value());

        list.add(node);

        if (tree.getChildrenAsList().size() > 0) {
            tree.getChildrenAsList().forEach(t -> {
                list.addAll(recursive(t));
            });
        }

        return list;
    }
}
