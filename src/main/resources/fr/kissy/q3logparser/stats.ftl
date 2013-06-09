<#import "includes/template.ftl" as template>

<@template.page>
    <h1>Stats</h1>
    <table class="table table-bordered" id="stats-list">
        <thead>
        <tr class="force-center">
            <th colspan="2">Player</th>
            <th colspan="3">Total</th>
            <th colspan="3">Average</th>
        </tr>
        <tr class="force-center sub-column">
            <th>Name</th>
            <th>Games</th>
            <th>Score</th>
            <th>Frags</th>
            <th>Deaths</th>
            <th>Score</th>
            <th>Frags</th>
            <th>Deaths</th>
        </tr>
        </thead>
        <tbody>
        <#list stats?values as stat>
            <tr class="force-center">
                <td class="force-left">
                    <a href="#modal_${stat_index}" data-toggle="modal">${stat.player}</a>
                </td>
                <td><span class="badge">${stat.games}</span></td>
                <td><span class="badge badge-inverse">${stat.score}</span></td>
                <td><span class="badge badge-inverse">${stat.frags}</span></td>
                <td><span class="badge badge-inverse">${stat.deaths} (${stat.suicides})</span></td>
                <td><span class="badge badge-success">${(stat.score / stat.games)?string("0.##")}</span></td>
                <td><span class="badge badge-success">${(stat.frags / stat.games)?string("0.##")}</span></td>
                <td><span class="badge badge-success">${(stat.deaths / stat.games)?string("0.##")} (${(stat.suicides / stat.games)?string("0.##")})</span></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@template.page>