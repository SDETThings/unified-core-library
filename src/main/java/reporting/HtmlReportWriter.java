package reporting;

import java.io.FileWriter;

public class HtmlReportWriter {

    public static void write(String path) {
        try (FileWriter w = new FileWriter(path)) {

            ReportManager rm = ReportManager.getInstance();

            /* ---------- HTML + HEAD ---------- */
            w.write("""
                    <html>
                    <head>
                    <style>
                        body {
                            font-family: Arial;
                            background-color: #0f172a;
                            color: #e5e7eb;
                        }
                    
                        h2 {
                            color: #38bdf8;
                        }
                    
                        details {
                            background-color: #020617;
                            border: 1px solid #1e293b;
                            border-radius: 8px;
                            padding: 10px;
                            margin-bottom: 10px;
                        }
                    
                        summary {
                            cursor: pointer;
                            font-weight: bold;
                        }
                    
                        .PASS { color: #22c55e; }
                        .FAIL { color: #ef4444; }
                        .SKIP { color: #facc15; }
                    .code-block {
                                background-color: #020617;
                                border: 1px solid #1e293b;
                                border-radius: 6px;
                                padding: 12px;
                                margin-top: 6px;
                                font-family: Consolas, monospace;
                                font-size: 13px;
                                color: #e5e7eb;
                                overflow-x: auto;
                                white-space: pre;
                            }
                        ul {
                            margin-left: 20px;
                        }
                    </style>
                    
                    <!-- Chart.js -->
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    </head>
                    <body>
                    """);

            /* ---------- SUMMARY ---------- */
            w.write(String.format("""
<div style="
    background: linear-gradient(135deg, #020617, #020617);
    border:1px solid #1e293b;
    border-radius:12px;
    padding:25px;
    margin-bottom:40px;
">

    <!-- Report Title -->
    <div style="margin-bottom:20px;">
        <h1 style="
            margin:0;
            color:#38bdf8;
            font-size:26px;
        ">Mini Automation Report</h1>

        <h3 style="
            margin-top:5px;
            color:#94a3b8;
            font-weight:normal;
        ">Execution Summary</h3>
    </div>

    <!-- Content -->
    <div style="
        display:flex;
        align-items:center;
        justify-content:space-between;
    ">

        <!-- LEFT: Stats -->
        <div style="
            display:flex;
            gap:30px;
        ">

            <div>
                <div class="PASS" style="font-size:28px; font-weight:bold;">%d</div>
                <div style="color:#94a3b8;">Passed</div>
            </div>

            <div style="border-left:1px solid #1e293b; padding-left:30px;">
                <div class="FAIL" style="font-size:28px; font-weight:bold;">%d</div>
                <div style="color:#94a3b8;">Failed</div>
            </div>

            <div style="border-left:1px solid #1e293b; padding-left:30px;">
                <div class="SKIP" style="font-size:28px; font-weight:bold;">%d</div>
                <div style="color:#94a3b8;">Skipped</div>
            </div>

        </div>

        <!-- Vertical Divider -->
        <div style="
            height:180px;
            width:1px;
            background-color:#1e293b;
            margin:0 40px;
        "></div>

        <!-- RIGHT: Chart -->
        <div style="width:220px; height:220px;">
            <canvas id="summaryChart"></canvas>
        </div>

    </div>
</div>

<hr style="
    border:0;
    border-top:1px solid #1e293b;
    margin-bottom:30px;
">
""",
                    rm.getPassed(),
                    rm.getFailed(),
                    rm.getSkipped()
            ));


            /* ---------- TEST DETAILS ---------- */
            for (ParentTest parent : rm.getAllParents()) {
                w.write("<details open>");
                w.write("<summary>" + parent.getName() + "</summary>");

                for (ChildTest child : parent.getChildren().values()) {
                    w.write("<details style='margin-left:20px'>");
                    w.write("<summary class='" + child.getFinalStatus() + "'>"
                            + child.getName() + "</summary>");

                    w.write("<ul>");
                    for (StepLog log : child.getLogs()) {
                        w.write("<li class='" + log.getStatus() + "'>"
                                + log.getStatus() + " - " + log.getMessage() + "</li>");
                    }
                    w.write("</ul>");
                    w.write("</details>");
                }
                w.write("</details>");
            }

            /* ---------- PIE CHART SCRIPT ---------- */
            w.write("<script>\n");

            w.write("const passed = " + rm.getPassed() + ";\n");
            w.write("const failed = " + rm.getFailed() + ";\n");
            w.write("const skipped = " + rm.getSkipped() + ";\n");

            w.write("""
                    new Chart(document.getElementById('summaryChart'), {
                               type: 'doughnut',
                               data: {
                                   labels: ['Passed', 'Failed', 'Skipped'],
                                   datasets: [{
                                       data: [passed, failed, skipped],
                                       backgroundColor: ['#22c55e', '#ef4444', '#facc15']
                                   }]
                               },
                               options: {
                                   plugins: {
                                       legend: {
                                           position: 'top',
                                           align: 'center',
                                           labels: {
                                               color: '#e5e7eb',
                                               boxWidth: 18,
                                               padding: 15,
                                               font: {
                                                   size: 14,
                                                   weight: 'bold'
                                               }
                                           }
                                       }
                                   },
                                   cutout: '65%'
                               }
                           });
                           </script>
                           </body>
                           </html>
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
