package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.events.body.ReportEvent;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class ReportStatusChangeEvent extends ReportEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final Report report;
    @Getter
    private final ReportStatus
            beforeStatusChange,
            newStatusChange;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
