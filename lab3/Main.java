public class Main {
    public static void main(String[] args)
    {
        MinimizeNormalForm min = new MinimizeNormalForm();
//        min.setLogicForm("(abcd!e)|(!abc!d!e)|(!abcd!e)|(abcde)|(abc!d!e)|(!a!b!c!d!e)");
//        min.setLogicForm("(abc!d)&(ab!cd)&(ab!c!d)&(a!bcd)&(a!bc!d)&(a!b!cd)&(a!b!c!d)&(!abcd)&(!abc!d)&(!ab!cd)&(!ab!c!d)&(!a!bcd)&(!a!bc!d)&(!a!b!cd)");
//        min.setLogicForm("(a|b)&(c|d)&(a->!c)");
//        MethodOfMinimization method = new MethodOfMinimization("!a->(b|c)");
//        method.createPCNFByTableMethod();
//        method.createPDNFByTableMethod();
//        method.createPDNFByCalculatedMethod();
//        method.createPCNFByCalculatedMethod();
                            MethodCarnot methodCarnot = new MethodCarnot();
                            methodCarnot.setLogicFormula("(a|b)&(c|d)&(a->!c)");
                            methodCarnot.setLogicFormula("a~b->c&d");
                            methodCarnot.minimizePCNF();
//        min.setSourceForm("a&c|d->e|b&!e");
//        min.setSourceForm("(!a!b!c!d)|(abcd)");
//        (¬a∨b∨c)∧(a∨¬b∨¬c)∧(a∨¬b∨c)∧(a∨b∨¬c)∧(a∨b∨c)
//        (¬a∧b∧c)∨(a∧¬b∧¬c)∨(a∧¬b∧c)∨(a∧b∧¬c)∨(a∧b∧c)
//        min.setLogicForm("a&c->b");
//        min.minimizePCNF();
//        min.minimizePCNFByTable();
//        min.minimizePDNF();
//        min.minimizePDNF();
//        min.minimizePDNFByTable();
//        min.minimizePDNF();
//        min.minimizePDNF();
//        min.minimizePDNFByTable();
//        min.setSourceForm("(abcd!e)|(!abc!d!e)|(!abcd!e)|(abcde)|(abc!d!e)|(!a!b!c!d!e)");
//        min.minimizeNormalForm();
//        min.minimizeByTableMethod();
//        min.minimizePDNF();
//        min.minimizePCNFByTable();
//        min.minimizeNormalForm();
//        min.minimizeByTableMethod();
//        min.printSourceForm();
    }
}