package com.paradigmx.cputile;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class CpuTileService extends TileService {

    @Override
    public void onTileAdded() {
        updateTile();
    }

    @Override
    public void onStartListening() {
        updateTile();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile.getState() == Tile.STATE_ACTIVE) {
            setCpuClusters(false);
            tile.setState(Tile.STATE_INACTIVE);
        } else {
            setCpuClusters(true);
            tile.setState(Tile.STATE_ACTIVE);
        }
        tile.updateTile();
    }

    private void updateTile() {
        Tile tile = getQsTile();
        if (tile == null) return;
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
    }

    private void setCpuClusters(boolean enable) {
        String value = enable ? "1" : "0";
        for (int i = 4; i <= 7; i++) {
            runAsRoot("echo " + value + " > /sys/devices/system/cpu/cpu" + i + "/online");
        }
    }

    private void runAsRoot(String command) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write((command + "\n").getBytes());
            process.getOutputStream().write("exit\n".getBytes());
            process.getOutputStream().flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
              }
