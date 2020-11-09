package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.service.HistoryService;
import co.jueyi.geekshop.types.history.HistoryEntryList;
import co.jueyi.geekshop.types.history.HistoryEntryListOptions;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class CustomerHistoryDataLoader implements MappedBatchLoaderWithContext<Long, HistoryEntryList> {

    private final HistoryService historyService;

    public CustomerHistoryDataLoader(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Override
    public CompletionStage<Map<Long, HistoryEntryList>> load(Set<Long> customerIds, BatchLoaderEnvironment env) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, HistoryEntryList> resultMap = new HashMap<>();
            for (Long customerId : customerIds) {
                Map<Object, Object> optionsMap = env.getKeyContexts();
                HistoryEntryListOptions options = (HistoryEntryListOptions) optionsMap.get(customerId);
                HistoryEntryList historyEntryList = historyService.getHistoryForCustomer(customerId, options);
                resultMap.put(customerId, historyEntryList);
            }
            return resultMap;
        });
    }
}
