document.addEventListener("DOMContentLoaded", function() {

    const gridSelector = '.dashboard';
    const gridItemSelector = '.draggable';
    const gridItemHandleSelector = '.handle';
    const gridGutter = 32;

    var grids = document.querySelectorAll(gridSelector);

    for (grid of grids) {

        const pckry = new Packery(grid, {
            itemSelector: gridItemSelector,
            horizontalOrder: true,
            gutter: gridGutter
        });

        const draggables = grid.querySelectorAll(gridItemSelector);
        for (draggable of draggables) {
            var draggie = new Draggabilly(draggable, {handle: gridItemHandleSelector});
            pckry.bindDraggabillyEvents(draggie);
        }
    }
});

function counterEntriesFromDailyConsumption(data) {
    console.log(data);
    data.sort((a, b) => (a.date > b.date) ? 1 : -1)
    for (let i = 0; i < data.length; i++) {
        data[i].value /= 1000;
    }
    for (let i = 1; i < data.length; i++) {
        data[i].value += data[i-1].value;
    }
    for (let i = 1; i < data.length - 1; i++) {
        if (!data[i+1].date.endsWith('01')) {
            data[i] = null;
        }
    }
    console.log(data.filter(x => x != null));
    return data.filter(x => x != null);
}