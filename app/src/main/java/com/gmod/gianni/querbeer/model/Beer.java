package com.gmod.gianni.querbeer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gianni G Modica on 05/11/2016.
 */

public class Beer {

    @SerializedName("currentPage")
    @Expose
    private int currentPage;

    @SerializedName("numberOfPages")
    @Expose
    private int numberOfPages;

    @SerializedName("totalResults")
    @Expose
    private int totalResults;

    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<Data>();

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }


    public void setStatus(String status) {
        this.status = status;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
