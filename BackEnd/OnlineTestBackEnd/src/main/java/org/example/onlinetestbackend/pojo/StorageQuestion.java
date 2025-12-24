package org.example.onlinetestbackend.pojo;

public class StorageQuestion {
    private Integer qid; // 问题ID
    private String qmajor; // 问题所属专业
    private Integer qtype; // 问题类型
    private String qcontent; // 问题内容，可能包含问题和选项
    private String qanswer; // 问题答案
    private String qimg; // 问题图片的路径

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

    // 构造方法
    public StorageQuestion(Integer qid, String qmajor, Integer qtype, String qcontent, String qanswer, String qimg) {
        this.qid = qid;
        this.qmajor = qmajor;
        this.qtype = qtype;
        this.qcontent = qcontent;
        this.qanswer = qanswer;
        this.qimg = qimg;
    }

    // 将 StorageQuestion 转换为 PostQuestion
    public PostQuestion postReady() {
        PostQuestion postQuestion = new PostQuestion(qid, qmajor, qtype, null, null, qanswer, qimg, null);
        String[] strs = qcontent.split("/");
        String[] qchoice = new String[strs.length - 1];
        postQuestion.setQcontent(strs[0]);
        for (int i = 1; i < strs.length; i++) qchoice[i - 1] = strs[i];
        postQuestion.setQchoice(qchoice);
        return postQuestion;
    }

    // 重写 toString 方法，方便打印对象内容
    @Override
    public String toString() {
        return "{\n" +
                "\"qid\":\"" + qid + "\",\n" +
                "\"qmajor\":\"" + qmajor + "\",\n" +
                "\"qtype\":" + qtype + ",\n" +
                "\"qcontent\":\"" + qcontent + "\",\n" +
                "\"qanswer\":\"" + qanswer + "\",\n" +
                "\"qimg\":\"" + qimg + "\"\n" +
                '}';
    }
}