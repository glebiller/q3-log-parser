<#macro page>
    <@compress single_line=true>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Q3 Scores</title>
        <meta cssClass="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Le styles -->
        <link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
        <style type="text/css">
            <#include "/fr/kissy/q3logparser/includes/styles.css.ftl">
        </style>

        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
        <script src="http://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.2/html5shiv.js"></script>

        <![endif]-->
        <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
        <script src="http://cdnjs.cloudflare.com/ajax/libs/datatables/1.9.4/jquery.dataTables.min.js"></script>
        <script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
        <script type="text/javascript">
            <#include "/fr/kissy/q3logparser/includes/scripts.js.ftl">
        </script>
    </head>

    <body>
        <header class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">Q3 Results</a>
                </div>
                <ul class="nav navbar-nav">
                    <li><a href="/matches/">Matches</a></li>
                    <li><a href="/stats/">Stats</a></li>
                </ul>
            </div>
        </header>

        <div class="container">
            <#nested/>
        </div>

        <div class="container">
            <hr />
        </div>
    </body>
    </html>
    </@compress>
</#macro>