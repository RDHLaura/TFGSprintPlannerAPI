package com.tfg.sprintplannerapi.model;

import com.tfg.sprintplannerapi.utils.Constants;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Data
@MappedSuperclass
public class Audit implements Serializable {

    private static final long serialVersionUID = -2680969231588318759L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "deleted")
    private Integer deleted = 0;

    /**When object was created*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false, length = 19, updatable = false)
    private Date createDate;

    /**When object was last updated. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", length = 19)
    private Date updateDate;

    /**Creator of the object*/
    @Column(name = "create_user_id", length = 19)
    private Long createdBy;

    /** Last modifier of the object */
    @Column(name = "update_user_id", length = 19)
    private Long updatedBy;

    /**Update Audit Info*/
    @PreUpdate
    public void updateAuditInfo() throws ParseException {
        this.updateDate = getNow();
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal()!= null){
            this.updatedBy = getUserLoggedId();
        }
    }

    @PrePersist
    public void createAuditInfo() throws ParseException {

        Date now = getNow();
        this.createDate = now;
        this.updateDate = now;

        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal()!= null) {
            this.createdBy = getUserLoggedId();
            this.updatedBy = getCreatedBy();
        }

    }

    private Long getUserLoggedId (){

        Long userLoggedId = Constants.ANONYMOUS_USER;
        if (!SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .equals("anonymousUser")){
            userLoggedId = Long.valueOf(
                    ((User)SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal()
                    ).getUsername()
            );
        }
        return userLoggedId;

    }
    private Date getNow () throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date now = dateFormat.parse(Instant.now().toString());
        return now;
    }
}
