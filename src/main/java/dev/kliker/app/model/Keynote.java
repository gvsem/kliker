package dev.kliker.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@JsonSerialize
public class Keynote {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    @JsonView(View.OnCreate.class)
    @JsonProperty("id")
    private @Getter
    UUID id = UUID.randomUUID();

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private @Getter @Setter
    byte[] file;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private @Getter
    @JsonView(View.Public.class)
    @JsonProperty("createdAt")
    Date createdAt = new Date();

    private @Getter
    @JsonView(View.Public.class)
    @JsonProperty("currentSlide")
    Integer currentSlide = 0;

    private @Getter @Setter
    @JsonView(View.Public.class)
    @JsonProperty("pagesCount")
    Integer pagesCount = 0;

    @Column(name = "displayId", updatable = false, nullable = false, columnDefinition = "uuid")
    @JsonView(View.OnCreate.class)
    @JsonProperty("displayId")
    private @Getter UUID displayId = UUID.randomUUID();

    @Column(name = "clickerId", updatable = false, nullable = false, columnDefinition = "uuid")
    @JsonView(View.OnCreate.class)
    @JsonProperty("clickerId")
    private @Getter UUID clickerId = UUID.randomUUID();

    public Keynote nextSlide() {
        currentSlide = Math.min(pagesCount - 1, currentSlide + 1);
        return this;
    }

    public Keynote prevSlide() {
        currentSlide = Math.max(0, currentSlide - 1);
        return this;
    }

}
