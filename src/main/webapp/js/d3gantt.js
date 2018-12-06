d3.gantt = function () {
    var FIT_TIME_DOMAIN_MODE = "fit";
    var FIXED_TIME_DOMAIN_MODE = "fixed";
    
    var margin = {
        top : 0,
        right : 20,
        bottom : 20,
        left : 60
    };
    
    var domain_start = 0;
    var domain_end = 0;
    var domain_mode = FIT_TIME_DOMAIN_MODE; // fixed or fit
    var task_name = [];
    var reso_name = [];
    var height = document.body.clientHeight - margin.top - margin.bottom-5;
    var width = document.body.clientWidth - margin.right - margin.left-5;

    var keyFunction = function(d) {
        return d.startDate + d.taskName + d.endDate;
    };

    var rectTransform = function(d) {
        return "translate(" + x(d.startDate) + "," + y(d.taskName) + ")";
    };

    var x = d3.scale.linear()
            .domain([ domain_start, domain_end ])
            .range([ 0, width ])
            .clamp(true);
    var y = d3.scale.ordinal()
            .domain(task_name)
            .rangeRoundBands([ 0, height - margin.top - margin.bottom], .1);

    var xAxis = d3.svg.axis()
                .scale(x).orient("bottom")
                .tickFormat(function(x){return x;}).tickSubdivide(true).tickSize(1).tickPadding(0);
    var yAxis = d3.svg.axis()
                .scale(y).orient("left").tickSize(1);

    var initTimeDomain = function() {
        if (domain_mode === FIT_TIME_DOMAIN_MODE) {
            console.debug("FIT_TIME_DOMAIN_MODE");
            if (tasks === undefined || tasks.length < 1) {
                domain_start = 0;
                domain_end = 100;
                return;
            }
            tasks.sort(function(a, b) {
                return a.endDate - b.endDate;
            });
            domain_end = tasks[tasks.length - 1].endDate;
            tasks.sort(function(a, b) {
                return a.startDate - b.startDate;
            });

            // domain_start = tasks[0].startDate;
            // console.debug(domain_start);
            // console.debug(domain_end);
        } else {
            // console.debug("FIXED_TIME_DOMAIN_MODE");
        }
    };

    var initAxis = function() {
        // console.debug("task_name", task_name);
        // console.debug("domain_start", domain_start);
        // console.debug("domain_end", domain_end);
        x = d3.scale.linear()
            .domain([ domain_start, domain_end ])
            .range([ 0, width ])
            .clamp(true);
        y = d3.scale.ordinal()
            .domain(task_name)
            .rangeRoundBands([ 0, height - margin.top - margin.bottom ]);
        // for (var i = 0; i <task_name.length; i++) {
        //    console.debug(task_name[i], y(task_name[i]));
        // };
        xAxis = d3.svg.axis()
                .scale(x)
                .orient("bottom")
                .tickFormat(function(x){return x;}).tickSubdivide(true).tickSize(1).tickPadding(0);
        yAxis = d3.svg.axis()
                .scale(y)
                .orient("left")
                .tickSize(1);
    };
    
    function gantt(tasks) {

        initTimeDomain();
        initAxis();

        var svg = d3.select("#cnt-gantt")
                    .append("svg")
                    .attr("class", "chart")
                    .attr("width", width + margin.left + margin.right)
                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("class", "gantt-chart")
                    .attr("width", width + margin.left + margin.right)
                    .attr("height", height + margin.top + margin.bottom)
                    .attr("transform", "translate(" + margin.left + ", " + margin.top + ")");

        svg.selectAll(".chart")
           .data(tasks, keyFunction)
           .append("rect")
           .attr("rx", 0)
           .attr("ry", 0)
           .attr("fill", function(d){ 
                if(reso_name[d.resoName] == null){ return "bar";}
                return reso_name[d.resoName];
            })
           .attr("y", 0)
           .attr("transform", rectTransform)
           .attr("height", function(d) { return y.rangeBand(); })
           .attr("width", function(d) { 
                return (x(d.endDate) - x(d.startDate)); 
            });

        svg.append("g")
           .attr("class", "x axis")
           .attr("transform", "translate(0, " + (height - margin.top - margin.bottom) + ")")
           .transition()
           .call(xAxis);

        svg.append("g")
           .attr("class", "y axis")
           .transition()
           .call(yAxis);

        return gantt;

    };
    
    gantt.redraw = function(tasks) {

        initTimeDomain();
        initAxis();
        
        var svg = d3.select("svg");

        var ganttChartGroup = svg.select(".gantt-chart");
        var rect = ganttChartGroup.selectAll("rect").data(tasks, keyFunction);
        
        rect.enter()
            .insert("rect",":first-child")
            .attr("rx", 0)
            .attr("ry", 0)
            .attr("fill", function(d){ 
                if(reso_name[d.resoName] == null){ return "bar";}
                return reso_name[d.resoName];
            }) 
            .transition()
            .attr("y", 0)
            .attr("transform", rectTransform)
            .attr("height", function(d) { return y.rangeBand(); })
            .attr("width", function(d) { 
                return (x(d.endDate) - x(d.startDate)); 
            });

        rect.transition()
            .attr("transform", rectTransform)
            .attr("height", function(d) { return y.rangeBand(); })
            .attr("width", function(d) { 
                return x(d.endDate - d.startDate); 
            });
        
        rect.exit().remove();

        svg.select(".x").transition().call(xAxis);
        svg.select(".y").transition().call(yAxis);
        
        return gantt;
    };

    gantt.margin = function(value) {
        if (!arguments.length)
            return margin;
        margin = value;
        return gantt;
    };

    gantt.timeDomain = function(value) {
        if (!arguments.length)
            return [ domain_start, domain_end ];
            domain_start = +value[0], domain_end = +value[1];
            return gantt;
    };

    gantt.timeDomainMode = function(value) {
        if (!arguments.length)
            return domain_mode;
        domain_mode = value;
        return gantt;

    };

    gantt.taskName = function(value) {
        if (!arguments.length)
            return task_name;
        task_name = value;
        return gantt;
    };
    
    gantt.resoName = function(value) {
        if (!arguments.length)
            return reso_name;
        reso_name = value;
        return gantt;
    };

    gantt.width = function(value) {
        if (!arguments.length)
            return width;
        width = +value;
        return gantt;
    };

    gantt.height = function(value) {
        if (!arguments.length)
            return height;
        height = +value;
        return gantt;
    };


    return gantt;
};
