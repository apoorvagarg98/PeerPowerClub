package com.example.peerpowerclub.models;

public class coursemodel {
    public String courseLongDescription,courseShortDescription,courseName,courseimageuri,groupLink;
    public coursemodel(){

    }

    public coursemodel(String courseLongDescription, String courseShortDescription, String courseName, String courseimageuri,String groupLink) {
        this.courseLongDescription = courseLongDescription;
        this.courseShortDescription = courseShortDescription;
        this.courseName = courseName;
        this.courseimageuri = courseimageuri;
        this.groupLink = groupLink;
    }

    public String getCourseLongDescription() {
        return courseLongDescription;
    }

    public String getCourseShortDescription() {
        return courseShortDescription;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseimageuri() {
        return courseimageuri;
    }

    public String getGroupLink() {
        return groupLink;
    }
}
