function AutoResizeImage(max_weight, max_height, img_obj) 
{
    if (img_obj.width > max_weight) {
        img_obj.width = max_weight;
    }
    if (img_obj.height > max_height) {
        img_obj.height = max_height;
    }
}

function plot_generation (input) 
{
    $('#ctn-generation').highcharts({
        title: {
            text: 'A General Trend during Paralleled Calculation Based on Maximum, Minimum and Mean Value'
        },
        subtitle: {
            text: 'by Jeanhwea hujinghui@buaa.edu.cn'
        },
        xAxis: {
            categories: [],
            tickmarkPlacement: 'on',
            title: {
            	text: 'Number of Generation'
            },
            labels: {
                enabled: false
            }
        },
        yAxis: {
            title: {
                text: 'Average Duration'
            },
            labels: {
            }
        },
        tooltip: {
            shared: true,
            valueSuffix: ' days'
        },
        plotOptions: {
            area: {
                stacking: 'normal',
                lineColor: '#666666',
                lineWidth: 1,
                marker: {
                    lineWidth: 1,
                    lineColor: '#666666'
                }
            }
        },
        series: input
    });
}

(function() {

    var gantt_plot = {
        version : "1.0.0"
    }

    var tasks = [];
    var ibak = 0;
    var task_data = [];
    var gantt;

    gantt_plot.addTask = function () {
        if (ibak < task_data.length) {
            tasks.push(task_data[ibak]);
            ibak++;
            // console.debug(tasks);
            gantt.redraw(tasks);
        } else {
            console.debug("had added all tasks!!!");
        }

        return gantt_plot;
    };

    gantt_plot.removeTask = function () {
        if (ibak > 0) {
            tasks.pop();
            ibak--;
            // console.debug(tasks);
            gantt.redraw(tasks);
        } else {
            console.debug("have removed all tasks!!!");
        }
        return gantt_plot;
    };

    gantt_plot.addAllTasks = function () {
        if (ibak < task_data.length) {
        	for (; ibak < task_data.length; ibak++) {
        		tasks.push(task_data[ibak]);
        	}
        	gantt.redraw(tasks);
        } else {
            console.debug("had added all tasks!!!");
        }

        return gantt_plot;
    };
    
    gantt_plot.removeAllTasks = function() {
        if (ibak > 0) {
        	for (; ibak > 0; ibak--) {
        		tasks.pop();
        	}
            // console.debug(tasks);
            gantt.redraw(tasks);
        } else {
            console.debug("have removed all tasks!!!");
        }
        return gantt_plot;
    };

    gantt_plot.plot = function (data) {
        console.debug(data);
        task_data = data;

        tasks.push(task_data[ibak]);
        ibak++;
        var reso_names = {};

        var genColor = [
            "red",
            "blue",
            "green",
            "yellow",
            "black",
            "darkblue",
            "magenta",
            "bisque",
            "blanchedalmond",
            "seagreen",
            "blueviolet",
            "brown",
            "burlywood",
            "cadetblue",
            "chartreuse",
            "chocolate",
            "coral",
            "cornflowerblue",
            "cornsilk",
            "crimson",
            "cyan",
            "darkcyan",
            "darkgoldenrod",
            "darkgray",
            "darkgreen",
            "darkgrey",
            "darkkhaki",
            "darkmagenta",
            "darkolivegreen",
            "darkorange",
            "darkorchid",
            "darkred",
            "darksalmon",
            "darkseagreen",
            "darkslateblue",
            "darkslategray",
            "darkslategrey",
            "darkturquoise",
            "darkviolet",
            "deeppink",
            "deepskyblue",
            "dimgray",
            "dimgrey",
            "dodgerblue",
            "firebrick",
            "floralwhite",
            "forestgreen",
            "fuchsia",
            "gainsboro",
            "ghostwhite",
            "gold",
            "goldenrod",
            "gray",
            "aquamarine",
            "greenyellow",
            "grey",
            "honeydew",
            "hotpink",
            "indianred",
            "indigo",
            "ivory",
            "khaki",
            "lavender",
            "lavenderblush",
            "lawngreen",
            "lime",
            "limegreen",
            "linen",
            "maroon",
            "mediumaquamarine",
            "mediumblue",
            "mediumorchid",
            "mediumpurple",
            "mediumseagreen",
            "mediumslateblue",
            "mediumspringgreen",
            "mediumturquoise",
            "mediumvioletred",
            "midnightblue",
            "mintcream",
            "mistyrose",
            "moccasin",
            "navajowhite",
            "navy",
            "oldlace",
            "olive",
            "olivedrab",
            "orange",
            "orangered",
            "orchid",
            "palegoldenrod",
            "palegreen",
            "paleturquoise",
            "palevioletred",
            "papayawhip",
            "peachpuff",
            "peru",
            "pink",
            "plum",
            "powderblue",
            "purple",
            "rebeccapurple",
            "rosybrown",
            "royalblue",
            "saddlebrown",
            "salmon",
            "sandybrown",
            "seashell",
            "sienna",
            "silver",
            "skyblue",
            "slateblue",
            "slategray",
            "slategrey",
            "springgreen",
            "steelblue",
            "tan",
            "teal",
            "thistle",
            "tomato",
            "turquoise",
            "violet",
            "wheat",
            "white",
            "whitesmoke",
            "yellowgreen"
        ];

        var j = 0;
        for (var i = 0; i < task_data.length; i++) {
        	if (reso_names[task_data[i].resoName] === undefined) {
        		reso_names[task_data[i].resoName] = genColor[j];
        		j++;
        	}
        };
        console.debug(reso_names);
        var task_names = [];
        for (var i = 0; i < task_data.length; i++) {
           task_names[i] = task_data[i].taskName;
        };
        task_names.sort(function (a, b) {
            return +a.substr(1, a.length-1) - +b.substr(1, b.length-1);
        });

        tasks.sort(function(a, b) {
            return a.endDate - b.endDate;
        });
        var maxDate = tasks[tasks.length - 1].endDate;
        tasks.sort(function(a, b) {
            return a.startDate - b.startDate;
        });
        var minDate = tasks[0].startDate;


        var timeDomainString = "blank";
        startDomain = d3.min(task_data, function(d) { return d.startDate; });
        endDomain = d3.max(task_data, function(d) {return d.endDate;});

        gantt = d3.gantt().taskName(task_names)
                              .resoName(reso_names)
                              .timeDomain([startDomain, endDomain])
                              .height(15*data.length)
                              .width(window.document.body.offsetWidth-100);


        gantt.timeDomainMode("fixed");
        // changeTimeDomain(timeDomainString);

        // console.debug(tasks);
        gantt(tasks);
        gantt_plot.removeTask();

        return gantt_plot;
    }


    this.gantt_plot = gantt_plot;

})();