package co.jueyi.geekshop.types.settings;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ServerConfig {
    private List<OrderProcessState> orderProcess = new ArrayList<>();
    private List<String> permittedAssetTypes = new ArrayList<>();
}
