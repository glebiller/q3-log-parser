<#macro page>
    <@compress single_line=true>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Q3 Results</title>
        <meta cssClass="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Le styles -->
        <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css" rel="stylesheet">
        <style type="text/css">
            <#include "styles.css.ftl">
        </style>

        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

        <![endif]-->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
        <script src="http://cdnjs.cloudflare.com/ajax/libs/datatables/1.9.4/jquery.dataTables.min.js"></script>
        <script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
        <script type="text/javascript">
            <#include "scripts.js.ftl">
        </script>
    </head>

    <body>
        <div class="navbar">
            <div class="navbar-inner">
                <a class="brand" href="/">Q3 Results</a>
                <ul class="nav">
                    <li><a href="/games">Games</a></li>
                    <li><a href="/stats">Stats</a></li>
                </ul>
            </div>
        </div>

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