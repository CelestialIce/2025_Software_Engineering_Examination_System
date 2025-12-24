package org.example.onlinetestbackend.pojo;

public class PostQuestion {
    private Integer qid; // 题目ID
    private String qmajor; // 题目专业
    private Integer qtype; // 题目类型
    private String qcontent; // 题目内容
    private String[] qchoice; // 题目选项
    private String qanswer; // 题目答案
    private String qimg; // 题目图片
    private String useranswer; // 用户答案

    // Getter 和 Setter 方法
    public Integer getQid() {
        return qid;
    }

    public void setQid(Integer qid) {
        this.qid = qid;
    }

    public String getQmajor() {
        return qmajor;
    }

    public void setQmajor(String qmajor) {
        this.qmajor = qmajor;
    }

    public Integer getQtype() {
        return qtype;
    }

    public void setQtype(Integer qtype) {
        this.qtype = qtype;
    }

    public String getQcontent() {
        return qcontent;
    }

    public void setQcontent(String qcontent) {
        this.qcontent = qcontent;
    }

    public String getQanswer() {
        return qanswer;
    }

    public void setQanswer(String qanswer) {
        this.qanswer = qanswer;
    }

    public String getQimg() {
        return qimg;
    }

    public void setQimg(String qimg) {
        this.qimg = qimg;
    }

    public String getUseranswer() {
        return useranswer;
    }

    public void setUseranswer(String useranswer) {
        this.useranswer = useranswer;
    }

    public String[] getQchoice() {
        return qchoice;
    }

    public void setQchoice(String[] qchoice) {
        this.qchoice = qchoice;
    }

    // 构造方法
    public PostQuestion(Integer qid, String qmajor, Integer qtype, String qcontent, String[] qchoice, String qanswer, String qimg, String useranswer) {
        this.qid = qid;
        this.qmajor = qmajor;
        this.qtype = qtype;
        this.qcontent = qcontent;
        this.qanswer = qanswer;
        this.qimg = qimg;
        this.useranswer = useranswer;
        this.qchoice = qchoice;
    }

    /**
     * 将 PostQuestion 对象转换为 StorageQuestion 对象
     * @return 转换后的 StorageQuestion 对象
     */
    public StorageQuestion storageReady() {
        StorageQuestion storageQuestion = new StorageQuestion(qid, qmajor, qtype, null, qanswer, qimg);
        String fullContent = qcontent;
        for(String choice : qchoice) {
            fullContent += "/" + choice;
        }
        storageQuestion.setQcontent(fullContent);
        return storageQuestion;
    }
}