<#import "includes/template.ftl" as template>

<@template.page>
    <h1 class="align-team-score">Games</h1>
    <table class="table table-bordered table-games" id="games-list">
        <thead>
            <tr class="force-center">
                <th>Date</th>
                <th>Type</th>
                <th>Game</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
        <#list games?keys as game>
            <tr class="force-center">
                <td>${games[game][0]}</td>
                <td>${games[game][1]}</td>
                <td class="force-left">${games[game][2]}</td>
                <td><a class="btn btn-mini btn-primary" href="/games/${game}.html">View</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
    <p class="text-center">Number of games parsed : <strong>${games?size}</strong></p>
</@template.page>
