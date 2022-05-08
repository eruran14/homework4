package com.company;

import java.util.Random;

public class Main {
    public static int bossHealth = 1700;
    public static int bossDamage = 80;
    public static String bossDefence;
    public static int[] heroesHealth = {270, 260, 250, 260, 400, 230, 250, 270};
    public static int[] heroesDamage = {25, 20, 15, 0, 10, 15, 10, 15};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem",
            "Lucky", "Berserk", "Thor"};
    public static int roundNumber = 0;
    public static boolean isLuckyDodging = false;
    public static boolean isBossStunned = false;

    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            round();
        }
    }

    public static void round() {
        roundNumber++;
        Random random = new Random();
        isBossStunned = random.nextBoolean();
        if (isBossStunned) {
            if (heroesHealth[7] <= 0){
                isBossStunned = false;
            }
            else {
            System.out.println("****** Boss has been stunned by Thor and didn't attack this round!");
            heroesHits();
            printStatistics();
            }

        } else {
            chooseBossDefence();
            luckyDodging();
            golemTanking();
            berserk();
            bossHits();
            heroesHits();
            printStatistics();
            medicHeals();
        }
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length);
        if (randomIndex != 3) {
            bossDefence = heroesAttackType[randomIndex];
            System.out.println("Boss chose: " + bossDefence);
        } else {
            bossDefence = heroesAttackType[4];
            System.out.println("Boss chose: " + bossDefence);
        }
    }

    public static void printStatistics() {
        System.out.println(roundNumber + " ROUND___________________");
        System.out.println("Boss health: " + bossHealth + " (" + bossDamage + ")");
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " (" + heroesDamage[i] + ")");
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead)
            System.out.println("Boss won!!!");
        return allHeroesDead;
    }

    public static void bossHits() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] <= 0)
                continue;
            if (heroesHealth[i] - bossDamage < 0) {
                heroesHealth[i] = 0;
            } else {
                heroesHealth[i] = heroesHealth[i] - bossDamage;
            }
        }
    }

    public static void heroesHits() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (bossHealth <= 0 || heroesHealth[i] <= 0)
                continue;
            if (heroesAttackType[i] == bossDefence) {
                Random random = new Random();
                int coeff = random.nextInt(9) + 2;
                int criticalDamage = heroesDamage[i] * coeff;
                if (bossHealth - criticalDamage < 0) {
                    bossHealth = 0;
                } else
                    bossHealth = bossHealth - criticalDamage;
                System.out.println("Critical damage: " + criticalDamage);
            } else {
                if (bossHealth - heroesDamage[i] < 0) {
                    bossHealth = 0;
                } else
                    bossHealth = bossHealth - heroesDamage[i];
            }
        }
    }

    public static void medicHeals() {
        Random random = new Random();
        int healRate = random.nextInt(10, 40);
        for (int i = 0; i < heroesHealth.length && i != 3; i++) {
            if (heroesHealth[3] <= 0) {
                break;
            } else {
                if (heroesHealth[i] < 100 && heroesHealth[i] > 0) {
                    Random rand = new Random();
                    int whoToHeal = rand.nextInt(heroesHealth.length);
                    if (whoToHeal == 3) {
                        continue;
                    } else {
                        heroesHealth[whoToHeal] = heroesHealth[whoToHeal] + healRate;
                        System.out.println("Medic healed " + heroesAttackType[whoToHeal] + " by " + healRate);
                        break;
                    }
                }
            }
        }

    }

    public static void golemTanking() {
        Random random = new Random();
        boolean isGolemTanking = random.nextBoolean();
        for (int i = 0; i < heroesHealth.length; i++) {
            if (isGolemTanking && i != 4) {
                if (heroesHealth[i] <= 0 || i == 5 || heroesHealth[4] <= 0) {
                    continue;
                }
                heroesHealth[i] = heroesHealth[i] + (int) (bossDamage * 0.2);
                heroesHealth[4] = heroesHealth[4] - (int) (bossDamage * 0.2);
                if (heroesHealth[4] - (int) (bossDamage * 0.2) < 0) {
                    heroesHealth[4] = 0;
                }
                System.out.println(heroesAttackType[4] + " tanked " + (int) (bossDamage * 0.2) + " damage"
                        + " and helped " + heroesAttackType[i]);
            }
        }
    }

    public static void luckyDodging() {
        Random random = new Random();
        isLuckyDodging = random.nextBoolean();
        if (isLuckyDodging && heroesHealth[5] > 0) {
            heroesHealth[5] = heroesHealth[5] + bossDamage;
            System.out.println(heroesAttackType[5] + " dodged Boss's attack");
        }
    }

    public static void berserk() {
        Random random = new Random();
        int blockedDamage = random.nextInt(0, bossDamage);
        if (heroesHealth[6] <= 0){
            heroesHealth[6] = 0;
        }
        else {
        heroesHealth[6] = heroesHealth[6] + blockedDamage;
        if(bossHealth - blockedDamage < 0){
            bossHealth = 0;
        }
        else
        bossHealth = bossHealth - blockedDamage;
        System.out.println(heroesAttackType[6] + " blocked " + blockedDamage + " damage and returned it to the Boss");
        }
    }
}