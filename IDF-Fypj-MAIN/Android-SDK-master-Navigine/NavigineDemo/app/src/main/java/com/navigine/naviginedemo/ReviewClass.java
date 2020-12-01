package com.navigine.naviginedemo;

public class ReviewClass {
    private String locotion;
    private float rating;
    private String feedback;

    public ReviewClass (){
    }

    public ReviewClass(String locotion, float rating, String feedback){
        this.locotion =locotion;
        this.rating=rating;
        this.feedback=feedback;
    }

    public String getLocotion() {
        return locotion;
    }

    public void setLocotion(String locotion) {
        this.locotion = locotion;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

}
