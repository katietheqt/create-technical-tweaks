package cat.katie.createcreativetweaks.features.tick;

import cat.katie.createcreativetweaks.infrastructure.rendering.CCTColors;
import cat.katie.createcreativetweaks.util.CCTLang;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.createmod.catnip.theme.Color;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class TickCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("tick")
                .then(Commands.literal("freeze")
                        .executes(ctx -> {
                            if (TickRateManager.INSTANCE.isFrozen()) {
                                ctx.getSource().sendFailure(
                                        CCTLang.translate("command.tick.freeze.already_frozen")
                                                .color(CCTColors.RED)
                                                .component()
                                );
                                return 0;
                            } else {
                                TickRateManager.INSTANCE.freeze();
                                ctx.getSource().sendSuccess(() ->
                                        CCTLang.translate("command.tick.freeze.success")
                                                .color(CCTColors.ICE_BLUE)
                                                .component(),
                                        true
                                );
                                return Command.SINGLE_SUCCESS;
                            }
                        }))
                .then(Commands.literal("unfreeze")
                        .executes(ctx -> {
                            if (!TickRateManager.INSTANCE.isFrozen()) {
                                ctx.getSource().sendFailure(
                                        CCTLang.translate("command.tick.unfreeze.not_frozen")
                                                .color(CCTColors.RED)
                                                .component()
                                );
                                return 0;
                            } else {
                                TickRateManager.INSTANCE.unfreeze();
                                ctx.getSource().sendSuccess(() ->
                                        CCTLang.translate("command.tick.unfreeze.success")
                                                .color(Color.SPRING_GREEN)
                                                .component(),
                                        true
                                );
                                return Command.SINGLE_SUCCESS;
                            }
                        }))
                .then(Commands.literal("step")
                        .executes(ctx -> {
                            if (!TickRateManager.INSTANCE.isFrozen()) {
                                ctx.getSource().sendFailure(
                                        CCTLang.translate("command.tick.step.not_frozen")
                                                .color(CCTColors.RED)
                                                .component()
                                );
                                return 0;
                            } else {
                                TickRateManager.INSTANCE.step(1);
                                ctx.getSource().sendSuccess(() ->
                                                CCTLang.translate("command.tick.step.success")
                                                        .color(Color.SPRING_GREEN)
                                                        .component(),
                                        true
                                );
                                return Command.SINGLE_SUCCESS;
                            }
                        }));
    }
}
