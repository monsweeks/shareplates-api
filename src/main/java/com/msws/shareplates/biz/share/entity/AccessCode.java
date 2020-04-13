package com.msws.shareplates.biz.share.entity;

import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.data.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Builder
@Table(name = "access_code")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccessCode extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "name")
    private String code;

    @OneToOne
    @JoinColumn(name = "share_id", updatable = false, insertable = false)
    private Share share;

    @OneToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;
}
