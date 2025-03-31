package me.elpomoika.eidea.util.future;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class BackItem extends PageItem {

    public BackItem(boolean forward) {
        super(false);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        builder.setDisplayName("Предыдущая страница")
                .addLoreLines(gui.hasPreviousPage()
                        ? "Перейти на страницу " + gui.getCurrentPage() + "/" + gui.getPageAmount()
                        : "Назад уже нету страниц");

        return builder;
    }
}
