<#import "/fr/kissy/q3logparser/includes/template.ftl" as template>

<@template.page>
    <h1>Matches</h1>
    <table class="table table-bordered table-matches" id="matches-list">
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
        <#list matches?keys as hash>
            <#assign match=matches[hash]>
            <tr class="force-center">
                <td>${match.date}</td>
                <td>${match.typeName}</td>
                <td class="force-left"><#include "/fr/kissy/q3logparser/includes/title.ftl"></td>
                <td>${match.players?size}</td>
                <td><a class="btn btn-xs btn-default" href="/matches/${hash}/">View</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
    <p class="text-center">Number of matches parsed : <strong>${matches?size}</strong></p>
</@template.page>
