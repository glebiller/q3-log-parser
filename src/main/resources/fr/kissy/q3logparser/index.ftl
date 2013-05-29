<#import "includes/template.ftl" as template>

<@template.page>
    <h1 class="align-team-score">Games</h1>
    <table class="table table-bordered table-games" id="games-list">
        <tbody>
        <#list games?keys as game>
            <tr>
                <td><h4>${games[game]}</h4></td>
                <td class="force-center"><a class="btn btn-primary" href="/games/${game}.html">View</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@template.page>
