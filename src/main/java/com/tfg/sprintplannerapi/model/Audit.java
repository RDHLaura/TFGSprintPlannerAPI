package com.tfg.sprintplannerapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tfg.sprintplannerapi.utils.Constants;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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
    private Boolean deleted = false;

    /**When object was created*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false, length = 19, updatable = false)
    private Date createDate;

    /**When object was last updated. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", length = 19)
    private Date updateDate;

    /**Creator of the object*/
    @Column(name = "create_user_id", length = 50)
    private String createdBy = "anonymous";

    /** Last modifier of the object */
    @Column(name = "update_user_id", length = 50)
    private String updatedBy = null;

    /**Update Audit Info*/
    @PreUpdate
    public void updateAuditInfo() throws ParseException {
        this.updateDate = getNow();
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal()!= null){
            this.updatedBy = getUserLoggedEmail();
        }
    }

    @PrePersist
    public void createAuditInfo() throws ParseException {

        Date now = getNow();
        this.createDate = now;
        this.updateDate = now;

        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal()!= null) {
            this.createdBy = getUserLoggedEmail();
            this.updatedBy = getCreatedBy();
        }

    }

    private String getUserLoggedEmail(){

        String userLoggedEmail = "anonymous";
        if (!SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .equals("anonymousUser")){
            User userLogged = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userName = userLogged.getUsername();
            userLoggedEmail = userName;

        }
        return userLoggedEmail;

    }
    private Date getNow () throws ParseException{
        Calendar today = Calendar.getInstance();
        //today.set(Calendar.HOUR_OF_DAY, 0);

        Date now = today.getTime();
       /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date now = dateFormat(date);
                //dateFormat.parse(Instant.now().toString());*/
        return now;
    }
}
