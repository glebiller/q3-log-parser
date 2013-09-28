<#import "/fr/kissy/q3logparser/includes/template.ftl" as template>

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
                <td class="force-left">${stat.player}</td>
                <td><span class="badge">${stat.matches}</span></td>
                <td>${stat.score}</td>
                <td>${stat.frags}</td>
                <td>${stat.deaths} (${stat.suicides})</td>
                <td>${(stat.score / stat.matches)?string("0.00")}</td>
                <td>${(stat.frags / stat.matches)?string("0.00")}</td>
                <td>${(stat.deaths / stat.matches)?string("0.00")} (${(stat.suicides / stat.matches)?string("0.00")})</td>
            </tr>
        </#list>
        </tbody>
    </table>
    <p class="text-center">Number of matches parsed : <strong>${matchesCount}</strong></p>
</@template.page>