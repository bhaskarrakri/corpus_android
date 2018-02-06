package com.video.corpus.pojos;

import java.io.Serializable;

/**
 * Created by Bhaskar.c on 12/28/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class homecontent_model implements Serializable{

    public int getServiceassetid() {
        return serviceassetid;
    }

    public void setServiceassetid(int serviceassetid) {
        this.serviceassetid = serviceassetid;
    }


    private  String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private  String image;

    public String getMediacontent() {
        return mediacontent;
    }

    public void setMediacontent(String mediacontent) {
        this.mediacontent = mediacontent;
    }

    private  String mediacontent;

    private String moviereleaseyear;
    private String moviesynopsis;
    private String movieruntime;
    private String moviecastcrew;

    public String getMoviereleaseyear() {
        return moviereleaseyear;
    }

    public void setMoviereleaseyear(String moviereleaseyear) {
        this.moviereleaseyear = moviereleaseyear;
    }

    public String getMoviesynopsis() {
        return moviesynopsis;
    }

    public void setMoviesynopsis(String moviesynopsis) {
        this.moviesynopsis = moviesynopsis;
    }

    public String getMovieruntime() {
        return movieruntime;
    }

    public void setMovieruntime(String movieruntime) {
        this.movieruntime = movieruntime;
    }

    public String getMoviecastcrew() {
        return moviecastcrew;
    }

    public void setMoviecastcrew(String moviecastcrew) {
        this.moviecastcrew = moviecastcrew;
    }

    public String getMoviecategory() {
        return moviecategory;
    }

    public void setMoviecategory(String moviecategory) {
        this.moviecategory = moviecategory;
    }

    private String moviecategory;

    public boolean isIsmetadata() {
        return ismetadata;
    }

    public void setIsmetadata(boolean ismetadata) {
        this.ismetadata = ismetadata;
    }

    private boolean ismetadata;

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    private  int likeCount;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    private boolean isFavourite;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private int categoryId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    private int Id;
    private int serviceassetid;

     private String programDescription;
     private String programId;

    public String getProgramDescription() {
        return programDescription;
    }

    public void setProgramDescription(String programDescription) {
        this.programDescription = programDescription;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }
}
