<#import "includes/template.ftl" as template>

<@template.page>
    <h1 class="align-team-score">Games</h1>
    <table class="table table-bordered table-games" id="games-list">
        <thead>
            <tr class="force-center">
                <th>Date</th>
                <th>Type</th>
                <th>Game</th>
                <th>Players</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
        <#list games?keys as hash>
            <#assign game=games[hash]>
            <tr class="force-center">
                <td>${game.date}</td>
                <td>${game.typeName}</td>
                <td class="force-left"><#include "includes/title.ftl"></td>
                <td>${game.players?size}</td>
                <td><a class="btn btn-mini btn-primary" href="/games/${hash}/">View</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
    <p class="text-center">Number of games parsed : <strong>${games?size}</strong></p>
</@template.page>
