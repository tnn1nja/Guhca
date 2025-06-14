package net.tnn1nja.guhca.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class Test extends AbstractCommand {

    public Test(){
        name = "test";
        description = "this is a test command";
        aliases = List.of("t");
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {

    }

    @Override
    public @NotNull Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return empty;
    }

}
