// This is an example of one possible way to make a reusable component (or 'plugin'), consisting of:
//  * A view model class, which gives a way to configure the component and to interact with it (e.g., by exposing currentPageIndex as an observable, external code can change the page index)
//  * A custom binding (ko.bindingHandlers.simpleGrid in this example) so a developer can place instances of it into the DOM
//     - in this example, the custom binding works by rendering some predefined templates using the ko.jqueryTmplTemplateEngine template engine
//
// There are loads of ways this grid example could be expanded. For example,
//  * Letting the developer override the templates used to create the table header, table body, and page links div
//  * Adding a "sort by clicking column headers" option
//  * Creating some API to fetch table data using Ajax requests
//  ... etc

(function () {
    // Private function
    function getColumnsForScaffolding(data) {
        if ((typeof data.length != 'number') || data.length == 0)
            return [];
        var columns = [];
        for (var propertyName in data[0])
            columns.push({ headerText: propertyName,action:propertyName, rowText: propertyName,rowLink:propertyName });
        return columns;
    }

    ko.productGrid = {
        // Defines a view model class you can use to populate a grid
        viewModel: function (configuration) {
            this.data = configuration.data;
            this.currentPageIndex = ko.observable(0);
            this.pageSize = configuration.pageSize || 5;

            // If you don't specify columns configuration, we'll use scaffolding
            this.columns = configuration.columns || getColumnsForScaffolding(ko.utils.unwrapObservable(this.data)); 

            this.itemsOnCurrentPage = ko.dependentObservable(function () {
                var startIndex = this.pageSize * this.currentPageIndex();
                return this.data.slice(startIndex, startIndex + this.pageSize);
            }, this);

            this.maxPageIndex = ko.dependentObservable(function () {
                return Math.ceil(ko.utils.unwrapObservable(this.data).length / this.pageSize);
            }, this);
        }
    };

    // Templates used to render the grid
    var templateEngine = new ko.jqueryTmplTemplateEngine();
    templateEngine.addTemplate("ko_productGrid_grid", "\
                    <ul id=\"sortable\" class=\"ko-grid selectable\" cellspacing=\"0\">\
                            {{each(i, row) itemsOnCurrentPage()}}\
                                <li class=\"\">\
                                    {{each(j, columnDefinition) columns}}\
                                        <div>{{if typeof columnDefinition.rowLink == 'function' }}\
    				                   		<a href=\"#\" data-bind=\"click: function(){columnDefinition.rowLink(row)}\">${action}</a>\
                                        	{{/if}}\
                             			{{if columnDefinition.headerText == 'img' }}\
                             					<img class=\"prodsmallimg\" src=\"${ typeof columnDefinition.rowText == 'function' ? columnDefinition.rowText(row) : row[columnDefinition.rowText] }\" >\
                             			{{else}}\
                             					${ typeof columnDefinition.rowText == 'function' ? columnDefinition.rowText(row) : row[columnDefinition.rowText] }\
                             			{{/if}}\
                             			</div>\
                                    {{/each}}\
                                </li>\
                            {{/each}}\
                    </ul>");
    templateEngine.addTemplate("ko_productGrid_pageLinks", "\
                    <div class=\"ko-grid-pageLinks wrap\">\
                        <span>Page:</span>\
                        {{each(i) ko.utils.range(1, maxPageIndex)}}\
                            <a href=\"#\" data-bind=\"click: function() { currentPageIndex(i) }, css: { selected: i == currentPageIndex() }\">\
                                ${ i + 1 }\
                            </a>\
                        {{/each}}\
                    </div>");

    // The "productGrid" binding
    ko.bindingHandlers.productGrid = {
        // This method is called to initialize the node, and will also be called again if you change what the grid is bound to
        update: function (element, viewModelAccessor, allBindingsAccessor) {
            var viewModel = viewModelAccessor(), allBindings = allBindingsAccessor();
            
            // Empty the element
            while(element.firstChild)
                ko.removeNode(element.firstChild);

            // Allow the default templates to be overridden
            var gridTemplateName      = allBindings.simpleGridTemplate || "ko_productGrid_grid",
                pageLinksTemplateName = allBindings.simpleGridPagerTemplate || "ko_productGrid_pageLinks";

            // Render the main grid
            var gridContainer = element.appendChild(document.createElement("DIV"));
            ko.renderTemplate(gridTemplateName, viewModel, { templateEngine: templateEngine }, gridContainer, "replaceNode");

            // Render the page links
            var pageLinksContainer = element.appendChild(document.createElement("DIV"));
            ko.renderTemplate(pageLinksTemplateName, viewModel, { templateEngine: templateEngine }, pageLinksContainer, "replaceNode");
        }
    };
})();