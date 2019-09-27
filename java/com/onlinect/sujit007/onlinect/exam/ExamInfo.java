package com.onlinect.sujit007.onlinect.exam;

/**
 * Created by Sujit007 on 2/21/2017.
 */

public class ExamInfo {

    private int id;
    private String examName;
    private String batchName;
    private int totalQues;
    private int totalTime;
    private int endTime;


    public ExamInfo(int id, String examName, String batchName, int totalQues, int totalTime) {
        this.id = id;
        this.examName = examName;
        this.batchName = batchName;
        this.totalQues = totalQues;
        this.totalTime = totalTime;
    }

    public ExamInfo(int id, String examName, String batchName, int totalQues, int totalTime, int endTime) {
        this.id = id;
        this.examName = examName;
        this.batchName = batchName;
        this.totalQues = totalQues;
        this.totalTime = totalTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public int getTotalQues() {
        return totalQues;
    }

    public void setTotalQues(int totalQues) {
        this.totalQues = totalQues;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
