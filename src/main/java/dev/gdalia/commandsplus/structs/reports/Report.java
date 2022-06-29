package dev.gdalia.commandsplus.structs.reports;

import java.time.Instant;
import java.util.UUID;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@RequiredArgsConstructor
public class Report {

    @Getter
    @NonNull
    private final UUID reportUuid;

    @Getter
    @NonNull
    private final UUID convicted;


    @Getter
    @NotNull
    private final UUID reporter;

    @Getter
    @NotNull
    private Instant sentAt;

    @Getter
    @NonNull
    private final Reason reason;

    @Getter
    @NonNull
    private ReportStatus status;

}
