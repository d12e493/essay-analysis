package davis.controller;

import davis.bo.Match;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import davis.service.ParserService;
import davis.service.RuleService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EssayController {

    @Autowired
    private ParserService parseService;

    @Autowired
    private RuleService ruleService;

    @RequestMapping("/")
    @ResponseBody
    String test() {
        return "Hello World!";
    }

    @RequestMapping("/analysis")
    @ResponseBody
    public String analysis(){

        String essay = "〔記者吳政峰／台北報導〕司法院26日院會決議通過「勞動事件法」草案，分為總則、勞動調解程序、訴訟程序、保全程序、附則等5章，合計53條條文，未來將在各級法院設置勞動法庭或專股，加入勞動事件審理，草案近日送行政院會銜，預計本會期送立法院審議。\n" +
                "司法院指出，草案重點除了設置勞動專庭，並擴大勞動事件範圍，將與勞動生活相關的民事爭議，包括建教合作、性別工作平等、就業歧視、職業災害、工會活動、競業禁止及其他因勞動關係所生侵權行為爭議等，納入勞動事件範圍。\n" +
                "司法院表示，勞動事件起訴前應行「勞動調解」程序，草案參考日本法制，以勞動法庭法官1人與具有處理勞資經驗的調解委員2人，共同組成「勞動調解委員會」進行勞動調解，促成兩造解決紛爭，提升當事人自主解決紛爭功能。\n" +
                "勞動調解委員會得依職權提出解決紛爭方案，如當事人未異議，即視為調解成立，以擴大勞動調解弭平紛爭之成效；如勞動調解不成立，調解聲請人未為反對續行訴訟，即應由參與勞動調解的法官續行訴訟，以一次辯論終結為原則。\n" +
                "司法院指出，為了減少勞工的訴訟障礙，勞工得於我國勞務提供地法院起訴，其被訴時亦得聲請移送至其選定有管轄權的法院；減徵或暫免徵收勞工起訴、上訴及聲請強制執行之裁判費、執行費；勞工得偕同所屬工會選派之適當人員到場擔任輔佐人；雇主就其依法令應備置之文書有提出之義務，同時強化當事人與第三人提出所持有證據方法或資料之責任，及減輕勞工關於工資及工作時間等事實之舉證責任。";

        List<String> notMatchString = new ArrayList<String>();

        System.out.println("before split");
        System.out.println(essay);

        // 進行句子切割，分成各別的一句話

        String[] essays = essay.split("，|、|；|。");

        System.out.println("after split");

        for(String e:essays){
        // 進行斷詞
            System.out.println("斷詞前："+e);

            List list = parseService.segment(e);
            System.out.println(list);

            String afterE = StringUtils.join(list," ");

            System.out.println("斷詞後："+afterE);

            Tree parse = parseService.parse(afterE);

            System.out.println("Stanford Parser：");
            System.out.println(parse);

            List<Match> matchList = ruleService.matchCheck(parse.toString());
            System.out.println("Rule match：");
            if(matchList.size()>0){
                System.out.println("有比對到");
                System.out.println(matchList);
            }else{
                System.out.println("無任何規則配對到");
                notMatchString.add(e);
            }
            System.out.println("------------------------------------------------------------");
        }

        return "ok";
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
