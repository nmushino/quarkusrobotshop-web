package io.quarkuscoffeeshop.web.domain.commands;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkuscoffeeshop.domain.CommandType;
import io.quarkuscoffeeshop.domain.OrderLineItem;
import io.quarkuscoffeeshop.domain.OrderSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

@RegisterForReflection
public class PlaceOrderCommand {

    private final CommandType commandType = CommandType.PLACE_ORDER;
    List<OrderLineItem> homerobotItems;
    List<OrderLineItem> prorobotItems;
    private String id;
    private String storeId;
    private OrderSource orderSource;
    private String rewardsId;
    private BigDecimal total;

    public PlaceOrderCommand() {
    }

    public PlaceOrderCommand(String id, String storeId, OrderSource orderSource, String rewardsId, List<OrderLineItem> homerobotItems, List<OrderLineItem> prorobotItems, BigDecimal total) {
        this.id = id;
        this.orderSource = orderSource;
        this.storeId = storeId;
        this.rewardsId = rewardsId;
        this.homerobotItems = homerobotItems;
        this.prorobotItems = prorobotItems;
        this.total = total;
    }

    public Optional<String> getRewardsId() {
        return Optional.ofNullable(rewardsId);
    }

    public Optional<List<OrderLineItem>> getHomerobotItems() {
        return Optional.ofNullable(homerobotItems);
    }

    public Optional<List<OrderLineItem>> getProrobotItems() {
        return Optional.ofNullable(prorobotItems);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlaceOrderCommand.class.getSimpleName() + "[", "]")
                .add("commandType=" + commandType)
                .add("homerobotItems=" + homerobotItems)
                .add("prorobotItems=" + prorobotItems)
                .add("id='" + id + "'")
                .add("storeId='" + storeId + "'")
                .add("orderSource=" + orderSource)
                .add("rewardsId='" + rewardsId + "'")
                .add("total=" + total)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOrderCommand that = (PlaceOrderCommand) o;
        return commandType == that.commandType &&
                Objects.equals(homerobotItems, that.homerobotItems) &&
                Objects.equals(prorobotItems, that.prorobotItems) &&
                Objects.equals(id, that.id) &&
                Objects.equals(storeId, that.storeId) &&
                orderSource == that.orderSource &&
                Objects.equals(rewardsId, that.rewardsId) &&
                Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, homerobotItems, prorobotItems, id, storeId, orderSource, rewardsId, total);
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getId() {
        return id;
    }

    public String getStoreId() {
        return storeId;
    }

    public OrderSource getOrderSource() {
        return orderSource;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setHomerobotItems(List<OrderLineItem> homerobotItems) {
        this.homerobotItems = homerobotItems;
    }

    public void setProrobotItems(List<OrderLineItem> prorobotItems) {
        this.prorobotItems = prorobotItems;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setOrderSource(OrderSource orderSource) {
        this.orderSource = orderSource;
    }

    public void setRewardsId(String rewardsId) {
        this.rewardsId = rewardsId;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
