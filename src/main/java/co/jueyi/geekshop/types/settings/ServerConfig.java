package co.jueyi.geekshop.types.settings;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ServerConfig {
    public List<OrderProcessState> orderProcess = new ArrayList<>();
    public List<String> permittedAssetTypes = new ArrayList<>();
}
