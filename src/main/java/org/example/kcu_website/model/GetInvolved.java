package org.example.kcu_website.model;

import jakarta.persistence.*;

@Entity
@Table(name = "get_involved")
public class GetInvolved {
    @Id
    @Column(name = "involved_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "semesterName")
    private String name;
    private String startDate;
    private String endDate;
    private String link;

    public GetInvolved() {
    }

    public GetInvolved(Long id, String name, String startDate, String endDate, String link) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
