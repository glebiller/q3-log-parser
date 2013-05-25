<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Q3 Results</title>
    <meta cssClass="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Le styles -->
    <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css" rel="stylesheet">
    <style type="text/css">
        <#include "results.css.ftl">
    </style>

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

    <![endif]-->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/datatables/1.9.4/jquery.dataTables.min.js"></script>
    <script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        <#include "results.js.ftl">
    </script>
</head>

<body>

<div class="navbar">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="#">Q3 Results</a>
            <div class="nav-collapse collapse">
                <ul class="nav">
                    <li><a href="#">Home</a></li>
                    <li><a href="#games">Games</a></li>
                    <li><a href="#stats">Stats</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>

<div class="container">
    <h1 class="align-team-score">[${game.typeName}] ${game.map}
        <small>
            <span class="label label">D</span> ${game.formattedDuration} -
            <span class="label label-important">R</span> ${game.redTeamScore} -
            <span class="label label-info">B</span> ${game.blueTeamScore}
        </small>
    </h1>
    <table class="table table-condensed table-bordered" id="game-results">
        <thead>
        <tr class="force-center">
            <th colspan="3">Player</th>
            <th colspan="6">Stats</th>
            <th colspan="2">Streak</th>
            <th colspan="3">Flags</th>
        </tr>
        <tr class="force-center sub-column">
            <th>T</th>
            <th>Name</th>
            <th>Efficiency</th>
            <th>Score</th>
            <th>Frags</th>
            <th>FPM</th>
            <th>Deaths</th>
            <th>Lifetime</th>
            <th>Efficiency</th>
            <th>Frags</th>
            <th>Deaths</th>
            <th>Captured</th>
            <th>Picked up</th>
            <th>Returned</th>
            <th>Efficiency</th>
        </tr>
        </thead>
        <tbody>
        <#list game.players?values as player>
            <#assign statsEfficiency = (100 * player.frags?size / (1 + player.frags?size + player.deaths?size))>
            <#assign flagEfficiency = (100 * (player.flag.returned + player.flag.captured) / (1 + player.flag.returned + player.flag.captured + player.flag.pickedUp))>
            <tr class="force-center">
                <td><span class="label label-${player.teamCssClass}">${player.teamName}</span></td>
                <td class="force-left">
                    <a href="#modal_${player_index}" data-toggle="modal">${player.name}</a>
                </td>
                <td><span class="badge">${((statsEfficiency + flagEfficiency) / 2)?string("0.#")} %</span></td>
                <td><span class="badge badge-inverse">${player.score}</span></td>
                <td><span class="badge badge-inverse">${player.frags?size}</span></td>
                <td><span class="badge badge-inverse">${(60 * player.frags?size / game.duration)?string("0.##")}</span></td>
                <td><span class="badge badge-inverse">${player.deaths?size} (${player.suicides?size})</span></td>
                <td><span class="badge badge-inverse">${(game.duration / player.deaths?size)?round}s</span></td>
                <td><span class="badge badge-inverse">${statsEfficiency?string("0.#")} %</span></td>
                <td><span class="badge badge-success">${player.streak.frag}</span></td>
                <td><span class="badge badge-success">${player.streak.death}</span></td>
                <td><span class="badge badge-warning">${player.flag.captured}</span></td>
                <td><span class="badge badge-warning">${player.flag.pickedUp}</span></td>
                <td><span class="badge badge-warning">${player.flag.returned}</span></td>
                <td><span class="badge badge-warning">${flagEfficiency?string("0.#")} %</span></td>
            </tr>
        </#list>
        </tbody>
    </table>

    <#list game.players?values as player>
        <div id="modal_${player_index}" class="modal hide fade" tabindex="-1" role="dialog">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">${player.name}</h3>
            </div>
            <div class="modal-body">
                <h4>Weapons details</h4>
                <table class="table table-bordered">
                    <tr class="force-center">
                        <th>Weapon</th>
                        <th>Frags</th>
                        <th>Percent</th>
                    </tr>
                    <#list player.sortedWeaponKills as weapon>
                        <#if weapon.frags != 0>
                            <tr class="force-center">
                                <td>${weapon.meanOfDeathName}</td>
                                <td><span class="badge badge-inverse">${weapon.frags}</span></td>
                                <td><span class="badge badge-inverse">${100 * weapon.frags / player.frags?size} %</span></td>
                            </tr>
                        </#if>
                    </#list>
                </table>
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
            </div>
        </div>
    </#list>

</div>

</body>
</html>
