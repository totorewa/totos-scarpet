global_helmet_protects = true;
global_apply_slowness = true;
global_fire_duration = 140;
global_effect_duration = 60;

__config() -> {
    'stay_loaded' -> true,
    'scope' -> 'global'
};

__on_tick() -> if (tick_time() % 20 == 0, check_survival_players());

check_survival_players() -> if (weather() == 'clear', (
    day = day_time();
    if (day > 23599 || day < 12041, in_dimension('overworld', for(player('survival'), check_player(_))))
));

check_player(player) -> if (sky_light(player ~ 'pos') == 15, sun_damage_player(player));

sun_damage_player(player) -> (
    amp = 2;
    apply_mining_fatigue = global_apply_slowness;
    if (!global_helmet_protects || query(player, 'holds', 'head') == null, (
        modify(player, 'fire', global_fire_duration);
        apply_mining_fatigue = false;
        amp = 0
    ));

    if (query(player, 'effect', 'fire_resistance') == null, (
        if (global_apply_slowness, (
            modify(player, 'effect', 'slowness');
            modify(player, 'effect', 'slowness', global_effect_duration, amp)
        ));
        if (apply_mining_fatigue, modify(player, 'effect', 'mining_fatigue', global_effect_duration))
    ))
);
