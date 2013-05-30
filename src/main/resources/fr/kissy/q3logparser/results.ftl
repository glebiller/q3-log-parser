<#import "includes/template.ftl" as template>

<@template.page>
    <h1 class="align-team-score">[${game.typeName}] <#include "includes/title.ftl"></h1>
    <table class="table table-bordered" id="game-results">
        <thead>
        <tr class="force-center">
            <th colspan="<#if game.displayTeamInfos>3<#else>2</#if>">Player</th>
            <th colspan="6">Stats</th>
            <th colspan="2">Streak</th>
            <#if game.displayFlagInfos>
                <th colspan="3">Flags</th>
            </#if>
        </tr>
        <tr class="force-center sub-column">
            <#if game.displayTeamInfos>
                <th>T</th>
            </#if>
            <th>Name</th>
            <#if game.displayFlagInfos>
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
            <#if game.displayFlagInfos>
                <th>Captured</th>
                <th>Picked up</th>
                <th>Returned</th>
                <th>Efficiency</th>
            </#if>
        </tr>
        </thead>
        <tbody>
        <#list game.players?values as player>
            <#assign statsEfficiency = (100 * player.frags?size / (1 + player.frags?size + player.deaths?size))>
            <#if game.displayFlagInfos>
                <#assign flagEfficiency = (100 * (player.flag.returned + player.flag.captured) / (1 + player.flag.returned + player.flag.captured + player.flag.pickedUp))>
            </#if>
            <tr class="force-center">
                <#if game.displayTeamInfos>
                    <td><span class="label label-${player.teamCssClass}">${player.teamName}</span></td>
                </#if>
                <td class="force-left">
                    <a href="#modal_${player_index}" data-toggle="modal">${player.name}</a>
                </td>
                <#if game.displayFlagInfos>
                    <td><span class="badge">${((statsEfficiency + flagEfficiency) / 2)?string("0.#")} %</span></td>
                </#if>
                <td><span class="badge badge-inverse">${player.score}</span></td>
                <td><span class="badge badge-inverse">${player.frags?size}</span></td>
                <td><span class="badge badge-inverse">
                    <#if game.duration == 0>
                        -
                    <#else>
                        ${(60 * player.frags?size / game.duration)?string("0.##")}
                    </#if>
                </span></td>
                <td><span class="badge badge-inverse">${player.deaths?size} (${player.suicides?size})</span></td>
                <td><span class="badge badge-inverse">
                    <#if player.deaths?size == 0>
                        -
                    <#else>
                        ${(game.duration / player.deaths?size)?round}s
                    </#if>
                </span></td>
                <td><span class="badge badge-inverse">${statsEfficiency?string("0.#")} %</span></td>
                <td><span class="badge badge-success">${player.streak.frag}</span></td>
                <td><span class="badge badge-success">${player.streak.death}</span></td>
                <#if game.displayFlagInfos>
                    <td><span class="badge badge-warning">${player.flag.captured}</span></td>
                    <td><span class="badge badge-warning">${player.flag.pickedUp}</span></td>
                    <td><span class="badge badge-warning">${player.flag.returned}</span></td>
                    <td><span class="badge badge-warning">${flagEfficiency?string("0.#")} %</span></td>
                </#if>
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
                    <#list player.sortedWeaponKills as weaponKill>
                        <#if weaponKill.frags != 0>
                            <tr class="force-center">
                                <td>${weaponKill.meanOfDeathName}</td>
                                <td><span class="badge badge-inverse">${weaponKill.frags}</span></td>
                                <td><span class="badge badge-inverse">
                                    <#if player.frags?size == 0>
                                        -
                                    <#else>
                                        ${(100 * weaponKill.frags / player.frags?size)?string("0.##")} %
                                    </#if>
                                </span></td>
                            </tr>
                        </#if>
                    </#list>
                </table>
                <h4>Players details</h4>
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
                                <td><span class="badge badge-inverse">${playerKill.frags}</span></td>
                                <td><span class="badge badge-inverse">
                                    <#if player.frags?size == 0>
                                        -
                                    <#else>
                                        ${(100 * playerKill.frags / player.frags?size)?string("0.##")} %
                                    </#if>
                                </span></td>
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
</@template.page>