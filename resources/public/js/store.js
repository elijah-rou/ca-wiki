$(function() {
$(".imagecluster").each(function() {
    var wall = new freewall(this);
    wall.reset({
        selector: '.size320',
        cellW: function(container) {
            var cellWidth = 320;
            if (container.hasClass('size320')) {
                cellWidth = container.width()/2;
            }
            return cellWidth;
        },
        cellH: function(container) {
            var cellHeight = 320;
            if (container.hasClass('size320')) {
                cellHeight = container.height()/2;
            }
            return cellHeight;
        },
        fixSize: 0,
        gutterY: 20,
        gutterX: 20,
        onResize: function() {
            wall.fitWidth();
        }
    })
    wall.fitWidth();
});
$(window).trigger("resize");
});
