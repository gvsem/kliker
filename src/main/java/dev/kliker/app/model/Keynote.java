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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    @JsonView(View.OnCreate.class)
    @JsonProperty("id")
    private @Getter
    UUID id;

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
    Date createdAt;

    private @Getter
    @JsonView(View.Public.class)
    @JsonProperty("currentSlide")
    Integer currentSlide = 0;

    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "displayId", updatable = false, nullable = false)
    @JsonView(View.OnCreate.class)
    @JsonProperty("displayId")
    private UUID displayId;

    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "clickerId", updatable = false, nullable = false)
    @JsonView(View.OnCreate.class)
    @JsonProperty("clickerId")
    private UUID clickerId;

    public Keynote nextSlide() {
        currentSlide++;
        return this;
    }

    public Keynote prevSlide() {
        currentSlide = Math.max(0, currentSlide - 1);
        return this;
    }

}
