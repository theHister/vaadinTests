package com.example.application.views.list;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("list")
@Route(value = "list")
public class ListView extends VerticalLayout {

    private List<GridEntry> entries = new ArrayList<>();

    public ListView() {
        setSpacing(false);

        Grid<GridEntry> grid = new Grid<>(GridEntry.class,
                false);
        grid.addColumn(GridEntry::getName).setHeader("Entry name");
        grid.addComponentColumn(entry -> {
            GridEntryRenderer renderer = new GridEntryRenderer(entry);
            return renderer.getRenderedValue();
        }).setHeader("Entry value");

        for (int entryIdx = 0; entryIdx < 100; entryIdx++) {
            GridEntry entry = new GridEntry("entry_" + entryIdx,
                    String.valueOf(entryIdx));
            entries.add(entry);
        }

        grid.setItems(entries);
        add(grid);

        TextArea textArea = new TextArea();
        textArea.setHeight("25%");
        textArea.setWidth("75%");

        Button submitBtn = new Button("Submit");
        submitBtn.addClickListener(e -> {
            textArea.clear();
            StringBuilder text = new StringBuilder();
            text.append(LocalDateTime.now().toString()).append("\n");
            entries.forEach(entry -> text.append(entry.getName()).append("\t")
                    .append(entry.getValue()).append("\n"));
            textArea.setValue(text.toString());
        });
        add(submitBtn, textArea);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    class GridEntryRenderer {
        private GridEntry entry;
        TextArea textArea = new TextArea();

        public GridEntryRenderer(GridEntry entry) {
            this.entry = entry;

            Binder<GridEntryRenderer> binder = new Binder<>();
            binder.setBean(this);

            textArea.setEnabled(true);
            textArea.setReadOnly(false);
            textArea.setWidth(100, Unit.PERCENTAGE);
            Binder.BindingBuilder<GridEntryRenderer, String> valueBindingBuilder = binder
                    .forField(textArea);
            valueBindingBuilder.bind(GridEntryRenderer::getValue,
                    GridEntryRenderer::setValue);
            binder.readBean(this);
        }

        private TextArea getRenderedValue() {
            return textArea;
        }

        private String getValue() {
            return entry.getValue();
        }

        private void setValue(String newValue) {
            entry.setValue(newValue);
        }

        protected String getName() {
            return entry.getName();
        }

    }

    class GridEntry {
        private String name;
        private String value;

        GridEntry(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
