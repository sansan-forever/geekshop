package co.jueyi.geekshop.types.settings;

import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class GlobalSettings {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public Boolean trackInventory;
    public ServerConfig serverConfig;
}
