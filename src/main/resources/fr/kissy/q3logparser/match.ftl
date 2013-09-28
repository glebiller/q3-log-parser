<#import "/fr/kissy/q3logparser/includes/template.ftl" as template>

<@template.page>
    <h1 class="align-team-score">[${match.typeName}] <#include "/fr/kissy/q3logparser/includes/title.ftl"></h1>
    <table class="table table-bordered" id="match-results">
        <thead>
        <tr class="force-center">
            <th colspan="<#if match.displayTeamInfos>4<#else>3</#if>">Player</th>
            <th colspan="6">Stats</th>
            <th colspan="2">Streak</th>
            <#if match.displayFlagInfos>
                <th colspan="4">Flags</th>
            </#if>
        </tr>
        <tr class="force-center sub-column">
            <#if match.displayTeamInfos>
                <th>T</th>
            </#if>
            <th>Name</th>
            <th>Time</th>
            <#if match.displayFlagInfos>
                <th>Efficiency</th>
            </#if>
            <th>Score</th>
            <th>Frags</th>
            <th>FPM</th>
            <th>Deaths</th>
            <th>Lifetime</th>
            <th>Efficiency</th>
            <th>Frags</th>
            <th>Deaths</th>
            <#if match.displayFlagInfos>
                <th>Captured</th>
                <th>Picked up</th>
                <th>Returned</th>
                <th>Efficiency</th>
            </#if>
        </tr>
        </thead>
        <tbody>
        <#list match.players?values as player>
            <#assign statsEfficiency = (100 * player.frags?size / (1 + player.frags?size + player.deaths?size))>
            <#if match.displayFlagInfos>
                <#assign flagEfficiency = (100 * (player.flag.returned + player.flag.captured) / (1 + player.flag.returned + player.flag.captured + player.flag.pickedUp))>
            </#if>
            <tr class="force-center">
                <#if match.displayTeamInfos>
                    <td><span class="label label-${player.teamCssClass}">${player.teamName}</td>
                </#if>
                <td class="force-left">
                    <a href="#modal_${player_index}" data-toggle="modal">${player.name}</a>
                </td>
                <td>${player.formattedDuration}</td>
                <#if match.displayFlagInfos>
                    <td><span class="badge">${((statsEfficiency + flagEfficiency) / 2)?string("0.#")} %</span></td>
                </#if>
                <td>${player.score}</td>
                <td>${player.frags?size}</td>
                <td>
                    <#if player.duration == 0>
                        -
                    <#else>
                        ${(60 * player.frags?size / player.duration)?string("0.00")}
                    </#if>
                </td>
                <td>${player.deaths?size} (${player.suicides?size})</td>
                <td>
                    <#if player.deaths?size == 0>
                        -
                    <#else>
                        ${(player.duration / player.deaths?size)?round}s
                    </#if>
                </td>
                <td><span class="badge">${statsEfficiency?string("0.#")} %</span></td>
                <td>${player.streak.frag}</td>
                <td>${player.streak.death}</td>
                <#if match.displayFlagInfos>
                    <td>${player.flag.captured}</td>
                    <td>${player.flag.pickedUp}</td>
                    <td>${player.flag.returned}</td>
                    <td><span class="badge">${flagEfficiency?string("0.#")} %</span></td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
    <p class="text-center"><small>Match hash <code>${match.hash}</code>.</small></p>

    <#list match.players?values as player>
        <div id="modal_${player_index}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="modal_label_${player_index}" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 id="modal_label_${player_index}" class="modal-title">${player.name}</h4>
                    </div>
                    <div class="modal-body">
                        <h5>Weapons details</h5>
                        <table class="table table-bordered">
                            <tr class="force-center">
                                <th>Weapon</th>
                                <th>Frags</th>
                                <th>Percent</th>
                            </tr>
                            <#list player.sortedWeaponKills as weaponKill>
                                <#if weaponKill.frags != 0>
                                    <tr class="force-center">
                                        <td>${weaponKill.meanOfDeathName}</td>
                                        <td>${weaponKill.frags}</td>
                                        <td>
                                            <#if player.frags?size == 0>
                                                -
                                            <#else>
                                                ${(100 * weaponKill.frags / player.frags?size)?string("0.00")} %
                                            </#if>
                                        </td>
                                    </tr>
                                </#if>
                            </#list>
                        </table>
                        <br />
                        <h5>Players details</h5>
                        <table class="table table-bordered">
                            <tr class="force-center">
                                <th>Player</th>
                                <th>Frags</th>
                                <th>Percent</th>
                            </tr>
                            <#list player.sortedPlayerKills as playerKill>
                                <#if playerKill.frags != 0>
                                    <tr class="force-center">
                                        <td>${playerKill.player.name}</td>
                                        <td>${playerKill.frags}</td>
                                        <td>
                                            <#if player.frags?size == 0>
                                                -
                                            <#else>
                                                ${(100 * playerKill.frags / player.frags?size)?string("0.00")} %
                                            </#if>
                                        </td>
                                    </tr>
                                </#if>
                            </#list>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </#list>
</@template.page>