package cat.katie.createcreativetweaks.infrastructure;

import cat.katie.createcreativetweaks.features.tick.TickCommands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.createmod.catnip.command.CatnipCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;

@Mod.EventBusSubscriber
public class AllCommands {
    private AllCommands() {
        throw new UnsupportedOperationException("Cannot instantiate AllCommands");
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> tickCommands = TickCommands.register();
        registerIfNotPresent(dispatcher, tickCommands); // register as `/tick` if nobody else did

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("cct")
            .then(tickCommands);

        dispatcher.register(root);
    }

    private static <S> void registerIfNotPresent(CommandDispatcher<S> dispatcher, LiteralArgumentBuilder<S> builder) {
        CommandNode<S> node = dispatcher.findNode(Collections.singleton(builder.getLiteral()));

        if (node == null) {
            dispatcher.register(builder);
        }
    }
}
